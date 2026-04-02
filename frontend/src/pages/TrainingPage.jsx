import { useEffect, useState } from "react";
import TradeTable from "../components/TradeTable";
import { trainBot, getTrainingTrades } from "../services/api";

export default function TrainingPage() {
    const [trainingTrades, setTrainingTrades] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchTrainingTrades = async () => {
        try {
            const training = await getTrainingTrades();
            setTrainingTrades(training.data);
        } catch (err) {
            console.error("Training API error:", err);
        }
    };

    const handleTrain = async () => {
        try {
            setLoading(true);
            await trainBot();
            await fetchTrainingTrades();
        } catch (err) {
            console.error("Training failed:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTrainingTrades();
    }, []);

    return (
        <div>
            <div className="section-card">
                <h2 className="page-title">Training Mode</h2>
                <div className="button-row">
                    <button onClick={handleTrain} disabled={loading}>
                        {loading ? "Running Training..." : "Run Training"}
                    </button>
                </div>
            </div>

            <TradeTable title="Training Trades" trades={trainingTrades} />
        </div>
    );
}