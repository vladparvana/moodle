import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import { Modal, Button, Form } from "react-bootstrap";
import { AuthServiceClient } from "../generated/auth_grpc_web_pb";
import { RegisterUserRequest , UpdateUserRequest} from "../generated/auth_pb";

const AdminPage = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");
  const [showRegisterModal, setShowRegisterModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [newUser, setNewUser] = useState({ email: "", password: "", role: "user" });
  const [userToUpdate, setUserToUpdate] = useState({ email: "", role: "user" });
  const navigate = useNavigate();

  const client = new AuthServiceClient("http://localhost:8082");

  useEffect(() => {
    const role = Cookies.get("role");
    if (role !== "admin") {
      navigate("/dashboard");
    }

    const fetchUsers = async () => {
      try {
        const token = Cookies.get("jwt");
        const response = await axios.get("http://localhost:8080/api/academia/users", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setUsers(response.data);
      } catch (err) {
        setError("Eroare la incarcarea utilizatorilor.");
        console.error(err);
      }
    };

    fetchUsers();
  }, [navigate]);

  const handleRegister = () => {
    const registerUserRequest = new RegisterUserRequest();
    registerUserRequest.setEmail(newUser.email);
    registerUserRequest.setPassword(newUser.password);
    registerUserRequest.setRole(newUser.role);

    client.registerUser(registerUserRequest, {}, (err, response) => {
      if (err) {
        setError(`Eroare la inregistrare: ${err.message}`);
        return;
      }

      console.log(err);
      if (response.getSuccess()) {
        setUsers([...users, { email: newUser.email, role: newUser.role }]);
        setShowRegisterModal(false);
        setNewUser({ email: "", password: "", role: "user" });
        setError("");
      } else {
        setError(response.getMessage());
      }
    });
  };



  const handleUpdate = () => {
    const updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setEmail(userToUpdate.email);
    updateUserRequest.setPassword(userToUpdate.password);
    updateUserRequest.setRole(userToUpdate.role);

    client.updateUser(updateUserRequest, {}, (err, response) => {
      

      if (response.getSuccess()) {
        setUsers(
          users.map((user) =>
            user.email === userToUpdate.email ? { ...user, role: userToUpdate.role } : user
          )
        );
        setShowUpdateModal(false);
        setError("");
      } else {
        setError(response.getMessage());
      }
      if (err) {
        setError(`Eroare la actualizare: ${err.message}`);
        return;
      }
    });
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">Admin - Lista Utilizatorilor</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <Button className="mb-3" onClick={() => setShowRegisterModal(true)}>
        Înregistrează utilizator nou
      </Button>
      <table className="table table-striped">
        <thead>
          <tr>
            <th>Email</th>
            <th>Rol</th>
            <th>Acțiuni</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.email}>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>
                <Button
                  variant="warning"
                  className="me-2"
                  onClick={() => {
                    setUserToUpdate(user);
                    setShowUpdateModal(true);
                  }}
                >
                  Actualizează
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <Modal show={showRegisterModal} onHide={() => setShowRegisterModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Înregistrare utilizator nou</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                value={newUser.email}
                onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                placeholder="Introduceti email-ul"
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Parolă</Form.Label>
              <Form.Control
                type="password"
                value={newUser.password}
                onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                placeholder="Introduceti parola"
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Rol</Form.Label>
              <Form.Select
                value={newUser.role}
                onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
              >
                <option value="user">User</option>
                <option value="profesor">Profesor</option>
              </Form.Select>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowRegisterModal(false)}>
            Închide
          </Button>
          <Button variant="primary" onClick={handleRegister}>
            Înregistrează
          </Button>
        </Modal.Footer>
      </Modal>

    </div>
  );
};

export default AdminPage;
