import { useEffect, useMemo, useState } from "react";
import { getOverviewTrades } from "../services/api";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
} from "recharts";

function formatShortTime(timestamp) {
    const date = new Date(timestamp);
    if (Number.isNaN(date.getTime())) return timestamp;

    return date.toLocaleString("en-GB", {
        day: "2-digit",
        month: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });
}

function CustomTooltip({ active, payload }) {
    if (!active || !payload || !payload.length) {
        return null;
    }

    const data = payload[0].payload;

    return (
        <div
            style={{
                background: "#111827",
                border: "1px solid #334155",
                padding: "10px",
                borderRadius: "8px",
                color: "#e2e8f0",
            }}
        >
            <p style={{ margin: "0 0 6px 0" }}>
                <strong>Time:</strong> {data.label}
            </p>
            <p style={{ margin: "0 0 6px 0" }}>
                <strong>Portfolio Value:</strong> ${data.value}
            </p>
            <p style={{ margin: "0 0 6px 0" }}>
                <strong>Trade Type:</strong> {data.type}
            </p>
            <p style={{ margin: "0 0 6px 0" }}>
                <strong>Trade Price:</strong> ${data.price}
            </p>
            <p style={{ margin: "0 0 6px 0" }}>
                <strong>Quantity:</strong> {data.quantity}
            </p>
            <p style={{ margin: 0 }}>
                <strong>Trade Value:</strong> ${data.tradeValue}
            </p>
        </div>
    );
}

export default function OverviewPage() {
    const [mode, setMode] = useState("LIVE");
    const [trades, setTrades] = useState([]);

    const fetchOverview = async (selectedMode) => {
        try {
            const res = await getOverviewTrades(selectedMode);
            setTrades(res.data);
        } catch (err) {
            console.error("Overview fetch failed:", err);
        }
    };

    useEffect(() => {
        fetchOverview(mode);
    }, [mode]);

    const chartData = useMemo(() => {
        let cash = 10000;
        let btc = 0;

        return trades.map((trade, index) => {
            const price = Number(trade.price);
            const qty = Number(trade.quantity);

            if (trade.type === "BUY") {
                cash -= qty * price;
                btc += qty;
            } else if (trade.type === "SELL") {
                cash += qty * price;
                btc -= qty;
            }

            const totalValue = cash + btc * price;

            return {
                time: index,
                label: formatShortTime(trade.timestamp),
                value: Number(totalValue.toFixed(2)),
                price: Number(price.toFixed(2)),
                type: trade.type,
                quantity: Number(qty.toFixed(8)),
                tradeValue: Number((qty * price).toFixed(2)),
            };
        });
    }, [trades]);

    const latestValue =
        chartData.length > 0 ? chartData[chartData.length - 1].value : 10000;

    const returnPercent = (((latestValue - 10000) / 10000) * 100).toFixed(2);

    return (
        <div>
            <div className="section-card">
                <h2 className="page-title">Portfolio Value Overview</h2>

                <div className="button-row">
                    <button onClick={() => setMode("LIVE")}>Live Overview</button>
                    <button onClick={() => setMode("TRAINING")}>Training Overview</button>
                </div>

                <div className="stats-grid">
                    <div className="stat-box">
                        <h3>Mode</h3>
                        <p>{mode}</p>
                    </div>
                    <div className="stat-box">
                        <h3>Latest Portfolio Value</h3>
                        <p>${latestValue}</p>
                    </div>
                    <div className="stat-box">
                        <h3>Return %</h3>
                        <p>{returnPercent}%</p>
                    </div>
                </div>
            </div>

            <div className="section-card">
                <h2 className="page-title">Performance Over Time</h2>

                <div style={{ width: "100%", height: 400 }}>
                    <ResponsiveContainer>
                        <LineChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis
                                dataKey="time"
                                minTickGap={30}
                                tickFormatter={(value, index) => chartData[index]?.label || ""}
                            />
                            <YAxis />
                            <Tooltip content={<CustomTooltip />} />
                            <Line
                                type="monotone"
                                dataKey="value"
                                stroke="#60a5fa"
                                strokeWidth={2}
                                dot={false}
                                activeDot={{ r: 6 }}
                            />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
}