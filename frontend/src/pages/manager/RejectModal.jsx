import "./LeaveApprovalPage.css";

function RejectModal({ show, remarks, setRemarks, onClose, onConfirm }) {

  if (!show) return null;

  return (
    <div className="modal-overlay">

      <div className="modal">

        <h3>Reject Leave Request</h3>

        <textarea
          placeholder="Enter rejection remarks"
          value={remarks}
          onChange={(e) => setRemarks(e.target.value)}
        />

        <div className="modal-actions">

          <button
            className="modal-cancel"
            onClick={onClose}
          >
            Cancel
          </button>

          <button
            className="modal-confirm"
            onClick={onConfirm}
          >
            Reject
          </button>

        </div>

      </div>

    </div>
  );
}

export default RejectModal;