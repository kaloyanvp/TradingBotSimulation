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

        return trades.map((trade) => {
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
                time: formatShortTime(trade.timestamp),
                value: Number(totalValue.toFixed(2)),
                price: Number(price.toFixed(2)),
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
                            <XAxis dataKey="time" minTickGap={30} />
                            <YAxis />
                            <Tooltip />
                            <Line type="monotone" dataKey="value" dot={false} stroke="#60a5fa" />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
}