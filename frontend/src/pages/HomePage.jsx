import { useEffect, useState } from "react";
import Controls from "../components/Controls";
import Portfolio from "../components/Portfolio";
import TradeTable from "../components/TradeTable";
import { getAccount, getPortfolio, getLiveTrades } from "../services/api";

export default function HomePage() {
    const [balance, setBalance] = useState(0);
    const [btc, setBtc] = useState(0);
    const [liveTrades, setLiveTrades] = useState([]);

    const fetchData = async () => {
        try {
            const acc = await getAccount();
            const port = await getPortfolio();
            const live = await getLiveTrades();

            setBalance(acc.data.balance);
            setBtc(port.data.quantity);
            setLiveTrades(live.data);
        } catch (err) {
            console.error("API error:", err);
        }
    };

    useEffect(() => {
        fetchData();
        const interval = setInterval(fetchData, 3000);
        return () => clearInterval(interval);
    }, []);

    return (
        <div>
            <Controls />
            <Portfolio balance={balance} btc={btc} />
            <TradeTable title="Live Trades" trades={liveTrades} />
        </div>
    );
}