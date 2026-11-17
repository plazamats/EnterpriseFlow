import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../lib/api";

export function Login() {
  const [organisationName, setOrganisationName] = useState(""); const [email, setEmail] = useState(""); const [password, setPassword] = useState(""); const [error, setError] = useState(""); const navigate = useNavigate();
  async function submit(event: FormEvent) {
    event.preventDefault(); setError("");
    try { const { data } = await api.post("/auth/login", { organisationName, email, password }); localStorage.setItem("accessToken", data.accessToken); localStorage.setItem("refreshToken", data.refreshToken); navigate("/"); }
    catch { setError("Unable to sign in. Check your credentials and try again."); }
  }
  return <main className="auth-page"><form className="auth-card" onSubmit={submit}><p className="eyebrow">ENTERPRISEFLOW</p><h1>Welcome back</h1><p className="muted">Sign in to your service desk workspace.</p><label>Organisation<input required value={organisationName} onChange={e => setOrganisationName(e.target.value)} /></label><label>Email<input type="email" required value={email} onChange={e => setEmail(e.target.value)} /></label><label>Password<input type="password" required value={password} onChange={e => setPassword(e.target.value)} /></label>{error && <p className="error">{error}</p>}<button>Sign in</button></form></main>;
}
