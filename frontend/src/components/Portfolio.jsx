export default function Portfolio({ balance, btc }) {
    return (
        <div className="section-card">
            <h2 className="page-title">Portfolio Overview</h2>
            <div className="stats-grid">
                <div className="stat-box">
                    <h3>Balance</h3>
                    <p>${Number(balance).toFixed(2)}</p>
                </div>

                <div className="stat-box">
                    <h3>BTC Holdings</h3>
                    <p>{Number(btc).toFixed(8)}</p>
                </div>
            </div>
        </div>
    );
}