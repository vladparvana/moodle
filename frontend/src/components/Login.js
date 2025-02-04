import React, { useState, useEffect } from "react";
import { AuthServiceClient } from "../generated/auth_grpc_web_pb";
import { LoginRequest, TokenRequest } from "../generated/auth_pb";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const client = new AuthServiceClient("http://localhost:8082");

  useEffect(() => {
    const token = Cookies.get("jwt");
    if (token) {
      const tokenRequest = new TokenRequest();
      tokenRequest.setToken(token);

      client.validateToken(tokenRequest, {}, (err, response) => {
        if (err) {
          console.error("Eroare la validarea token-ului:", err);
          Cookies.remove("jwt");
        } else {
          console.log(response); 
          const isValid = response.u[1];
          console.log("Validare token:", isValid);
      
          if (isValid) {
            const decoded = jwtDecode(token);
            const role = decoded.role;
            Cookies.set("role",role);
            if (role === "admin") {
              navigate("/admin");
            } else {
              navigate("/dashboard");
            }
          } else {
            Cookies.remove("jwt");
          }
        }
      });
    }
  }, [navigate, client]);

  const handleLogin = () => {
    const loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    client.login(loginRequest, {}, (err, response) => {
      if (err) {
        setError(`Error: ${err.message}`);
        setMessage("");
      } else {
        const token = response.getToken();
        if (token) {
          Cookies.set("jwt", token, { expires: 1 }); 
          const decoded = jwtDecode(token);
          const role = decoded.role;

          setMessage("Autentificare cu succes");
          setError("");

          
          if (role === "admin") {
            navigate("/admin");
          } else {
            navigate("/dashboard");
          }
        } else {
          setError("Autentificarea a esuat.");
          setMessage("");
        }
      }
    });
  };

  return (
    <div className="d-flex vh-100 align-items-center justify-content-center bg-light">
      <div className="card shadow-lg" style={{ width: "24rem" }}>
        <div className="card-body">
          <h2 className="card-title text-center mb-4">Autentificare</h2>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              handleLogin();
            }}
          >
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="form-control"
                placeholder="Introdu email-ul"
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Parola
              </label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="form-control"
                placeholder="Introdu parola"
                required
              />
            </div>
            <button type="submit" className="btn btn-primary w-100">
              Autentificare
            </button>
          </form>
          {error && (
            <div className="alert alert-danger mt-4 text-center">{error}</div>
          )}
          {message && (
            <div className="alert alert-success mt-4 text-center">{message}</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Login;