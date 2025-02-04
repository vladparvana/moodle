import './App.css';
import Login from "./components/Login";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AdminPage from './components/AdminPage';
import UserPage from './components/UserPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/user" element={<UserPage />} />
      </Routes>
    </Router>
  );
}

export default App;
