import { useState } from "react";

export default function AdjustBalanceModal({ balance, onClose, onSave }) {
  const [newTotal, setNewTotal] = useState(balance.totalAllocated);

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-box"
        onClick={(e) => e.stopPropagation()} 
      >
        <div className="modal-header">
          <h3>Adjust Leave Balance</h3>
        </div>

        <div className="modal-body">
          <div className="modal-info">
            <strong>{balance.userName}</strong>
            <div>{balance.leaveTypeName} • {balance.year}</div>
            <div>Used: {balance.usedDays}</div>
            <div>Remaining: {balance.remainingDays}</div>
          </div>

          <label className="input-label">
            New Total Allocation
            <input
              type="number"
              value={newTotal}
              onChange={(e) => setNewTotal(e.target.value)}
            />
          </label>
        </div>

        <div className="modal-footer">
          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>

          <button
            className="btn-primary"
            onClick={() => onSave(balance.id, newTotal)}
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
}