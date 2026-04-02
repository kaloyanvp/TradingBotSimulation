import { startBot, pauseBot, resetBot } from "../services/api";

export default function Controls() {
    const handleReset = async () => {
        try {
            await resetBot();
            window.location.reload();
        } catch (err) {
            console.error("Reset failed:", err);
        }
    };

    return (
        <div className="section-card">
            <h2 className="page-title">Bot Controls</h2>

            <div className="button-row">
                <button onClick={startBot}>Start</button>
                <button onClick={pauseBot}>Pause</button>
            </div>

            <div className="button-row reset-row">
                <button className="reset-button" onClick={handleReset}>
                    Reset
                </button>
            </div>
        </div>
    );
}