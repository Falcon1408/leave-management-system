import { useState } from "react";

export default function HolidayModal({ holiday, onClose, onSave }) {
  const [name, setName] = useState(holiday?.name || "");
  const [startDate, setStartDate] = useState(holiday?.startDate || "");
  const [endDate, setEndDate] = useState(holiday?.endDate || "");

  const today = new Date().toISOString().split("T")[0];

  const handleSubmit = (e) => {
    e.preventDefault();

    const start = new Date(startDate);
    const end = new Date(endDate);
    const todayDate = new Date(today);

    if (end < start) {
      alert("End date cannot be before start date");
      return;
    }

    if (start < todayDate) {
      alert("Cannot create holidays in the past");
      return;
    }

    onSave({
      name,
      startDate,
      endDate
    });
  };

  return (
    <div className="modal-overlay">
      <div className="modal-container">

        <h3 className="modal-title">
          {holiday ? "Edit Holiday" : "Add Holiday"}
        </h3>

        <form onSubmit={handleSubmit} className="modal-form">

          <div className="form-group">
            <label>Holiday Name</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Start Date</label>
            <input
              type="date"
              value={startDate}
              min={today}
              onChange={(e) => setStartDate(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>End Date</label>
            <input
              type="date"
              value={endDate}
              min={startDate || today}
              onChange={(e) => setEndDate(e.target.value)}
              required
            />
          </div>

          <div className="modal-actions">

            <button type="button" onClick={onClose}>
              Cancel
            </button>

            <button type="submit" className="save-btn">
              Save
            </button>

          </div>

        </form>

      </div>
    </div>
  );
}