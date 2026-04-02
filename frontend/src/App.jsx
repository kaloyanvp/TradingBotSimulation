import { Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import TrainingPage from "./pages/TrainingPage";
import OverviewPage from "./pages/OverviewPage";
import "./App.css";

function App() {
    return (
        <div className="app">
            <header className="app-header">
                <h1>Crypto Trading Bot Dashboard</h1>

                <nav className="nav">
                    <Link to="/" className="nav-link">Live Mode</Link>
                    <Link to="/training" className="nav-link">Training Mode</Link>
                    <Link to="/overview" className="nav-link">Portfolio Overview</Link>
                </nav>
            </header>

            <main className="page-content">
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/training" element={<TrainingPage />} />
                    <Route path="/overview" element={<OverviewPage />} />
                </Routes>
            </main>
        </div>
    );
}

export default App;