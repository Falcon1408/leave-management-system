import { useEffect, useState } from "react";
import api from "../../services/api";

function CreateEmployee({ onClose, onSuccess }) {

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    password: "",
    role: "",
    managerId: "",
    joiningDate: ""
  });

  const [managers, setManagers] = useState([]);

  useEffect(() => {
    fetchManagers();
  }, []);

  const fetchManagers = async () => {
    try {
      const res = await api.get("/admin/employees?page=0&size=100");
      const managerList = res.data.content.filter(
        (emp) => emp.role === "MANAGER" && emp.status === "ACTIVE"
      );
      setManagers(managerList);
    } catch {
      console.error("Failed to fetch managers");
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const roleMap = {
        ADMIN: 1,
        MANAGER: 2,
        EMPLOYEE: 3
      };

      await api.post("/admin/employees", {
        firstName: form.firstName,
        lastName: form.lastName,
        username: form.username,
        email: form.email,
        password: form.password,
        roleId: roleMap[form.role],
        managerId: form.role === "EMPLOYEE"
          ? Number(form.managerId)
          : null,
        joiningDate: form.joiningDate
      });

      onSuccess();
      onClose();
    } catch {
      alert("Error creating employee");
    }
  };

  return (
    <div>
      <h3>Create Employee</h3>

      <form onSubmit={handleSubmit}>
        <input name="firstName" placeholder="First Name" onChange={handleChange} required />
        <input name="lastName" placeholder="Last Name" onChange={handleChange} required />
        <input name="username" placeholder="Username" onChange={handleChange} required />
        <input name="email" type="email" placeholder="Email" onChange={handleChange} required />
        <input name="password" type="password" placeholder="Password" onChange={handleChange} required />

        <select name="role" onChange={handleChange} required>
          <option value="">Select Role</option>
          <option value="ADMIN">ADMIN</option>
          <option value="MANAGER">MANAGER</option>
          <option value="EMPLOYEE">EMPLOYEE</option>
        </select>

        {form.role === "EMPLOYEE" && (
          <select name="managerId" onChange={handleChange} required>
            <option value="">Select Manager</option>
            {managers.map((m) => (
              <option key={m.id} value={m.id}>
                {m.firstName} {m.lastName}
              </option>
            ))}
          </select>
        )}

        <input name="joiningDate" type="date" onChange={handleChange} required />

        <button type="submit">Save</button>
        <button type="button" onClick={onClose}>Cancel</button>
      </form>
    </div>
  );
}

export default CreateEmployee;