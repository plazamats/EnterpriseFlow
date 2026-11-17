import React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Login } from "./pages/Login";
import { Dashboard } from "./pages/Dashboard";
import "./styles.css";

function App() {
  return <Routes><Route path="/login" element={<Login />} /><Route path="/" element={<Dashboard />} /><Route path="*" element={<Navigate to="/" replace />} /></Routes>;
}
createRoot(document.getElementById("root")!).render(<React.StrictMode><BrowserRouter><App /></BrowserRouter></React.StrictMode>);
