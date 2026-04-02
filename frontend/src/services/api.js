import axios from "axios";

const API = "http://localhost:8080";

export const startBot = () => axios.post(`${API}/bot/start`);
export const pauseBot = () => axios.post(`${API}/bot/pause`);
export const trainBot = () => axios.get(`${API}/bot/train`);
export const resetBot = () => axios.post(`${API}/bot/reset`);

export const getAccount = () => axios.get(`${API}/account`);
export const getPortfolio = () => axios.get(`${API}/portfolio`);

export const getLiveTrades = () => axios.get(`${API}/trades/live`);
export const getTrainingTrades = () => axios.get(`${API}/trades/training`);

export const getOverviewTrades = (mode = "LIVE") =>
    axios.get(`${API}/overview/trades?mode=${mode}`);