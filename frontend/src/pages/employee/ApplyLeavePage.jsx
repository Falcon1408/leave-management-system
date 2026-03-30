import { useEffect, useState } from "react";
import { applyLeave } from "../../services/api";
import { getLeaveTypes } from "../../services/api";
import "./EmployeeLeaves.css";
import MainLayout from "../../layout/MainLayout";

function ApplyLeavePage() {
  const [leaveTypes, setLeaveTypes] = useState([]);

  const [form, setForm] = useState({
    leaveTypeId: "",
    startDate: "",
    endDate: "",
    reason: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchLeaveTypes();
  }, []);

  const fetchLeaveTypes = async () => {
    try {
      const res = await getLeaveTypes();
      setLeaveTypes(res.data);
    } catch (err) {
      console.error("Failed to fetch leave types", err);
    }
  };

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);
      setMessage("");

      await applyLeave(form);

      setMessage("Leave request submitted successfully.");

      setForm({
        leaveTypeId: "",
        startDate: "",
        endDate: "",
        reason: "",
      });

    } catch (err) {
      setMessage(
        err.response?.data?.message || "Failed to submit leave request."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <MainLayout>
    <div className="employee-page">
      <h2>Apply Leave</h2>

      <form className="leave-form" onSubmit={handleSubmit}>

        <label>Leave Type</label>
        <select
          name="leaveTypeId"
          value={form.leaveTypeId}
          onChange={handleChange}
          required
        >
          <option value="">Select Leave Type</option>

          {leaveTypes.map((type) => (
            <option key={type.id} value={type.id}>
              {type.name}
            </option>
          ))}
        </select>

        <label>Start Date</label>
        <input
          type="date"
          name="startDate"
          value={form.startDate}
          onChange={handleChange}
          required
        />

        <label>End Date</label>
        <input
          type="date"
          name="endDate"
          value={form.endDate}
          onChange={handleChange}
          required
        />

        <label>Reason</label>
        <textarea
          name="reason"
          value={form.reason}
          onChange={handleChange}
        />

        <button className="submit-btn" type="submit" disabled={loading}>
          {loading ? "Submitting..." : "Submit Request"}
        </button>

        {message && (
          <p className="form-message">{message}</p>
        )}

      </form>
    </div>
    </MainLayout>
  );
}

export default ApplyLeavePage;