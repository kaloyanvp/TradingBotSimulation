function formatTimestamp(timestamp) {
    if (!timestamp) return "";

    const date = new Date(timestamp);

    if (Number.isNaN(date.getTime())) {
        return timestamp;
    }
    return date.toLocaleString();
}

export default function TradeTable({ title, trades }) {
    return (
        <div className="section-card">
            <h2 className="page-title">{title}</h2>

            <div className="table-wrapper">
                <table>
                    <thead>
                    <tr>
                        <th>Type</th>
                        <th>Qty</th>
                        <th>Price</th>
                        <th>Time</th>
                        <th>Mode</th>
                    </tr>
                    </thead>
                    <tbody>
                    {trades.length > 0 ? (
                        trades.map((t, i) => (
                            <tr key={i}>
                                <td>{t.type}</td>
                                <td>{t.quantity}</td>
                                <td>{t.price}</td>
                                <td>{formatTimestamp(t.timestamp)}</td>
                                <td>{t.mode}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5">No trades available.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}