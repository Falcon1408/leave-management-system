import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import MainLayout from "../../layout/MainLayout";
import api from "../../services/api";
import "./EmployeeDashboard.css";

const EmployeeDashboard = () => {

  const navigate = useNavigate();

  const [summary, setSummary] = useState({
    pending: 0,
    approved: 0,
    rejected: 0
  });

  const [recentRequests, setRecentRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchLeaveData();
  }, []);

  const fetchLeaveData = async () => {
    try {

      const res = await api.get("/leave-requests/my");
      const requests = res.data;

      const pending = requests.filter(r => r.status === "PENDING").length;
      const approved = requests.filter(r => r.status === "APPROVED").length;
      const rejected = requests.filter(r => r.status === "REJECTED").length;

      setSummary({
        pending,
        approved,
        rejected
      });

      setRecentRequests(requests.slice(0,5));

      setLoading(false);

    } catch (err) {
      console.error("Dashboard load error:", err);
      setLoading(false);
    }
  };

  return (
    <MainLayout>

      <div className="employee-dashboard">

        <h1>Employee Dashboard</h1>

        {loading ? (
          <p>Loading...</p>
        ) : (
          <>

            {/* Leave Request Summary */}

            <h2 className="section-title">Leave Request Summary</h2>

            <div className="summary-grid">

              <div className="summary-card">
                <h3>Pending</h3>
                <p>{summary.pending}</p>
              </div>

              <div className="summary-card">
                <h3>Approved</h3>
                <p>{summary.approved}</p>
              </div>

              <div className="summary-card">
                <h3>Rejected</h3>
                <p>{summary.rejected}</p>
              </div>

            </div>


            {/* Quick Actions */}

            <h2 className="section-title">Quick Actions</h2>

            <div className="actions-grid">

              <div
                className="action-card"
                onClick={() => navigate("/employee/apply")}
              >
                <h3>Apply Leave</h3>
                <p>Submit a new leave request</p>
              </div>

              <div
                className="action-card"
                onClick={() => navigate("/employee/history")}
              >
                <h3>Leave History</h3>
                <p>View all your leave requests</p>
              </div>

            </div>


            {/* Recent Requests */}

            <h2 className="section-title">Recent Leave Requests</h2>

            <table className="requests-table">

              <thead>
                <tr>
                  <th>Leave Type</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>Days</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>

                {recentRequests.length === 0 ? (
                  <tr>
                    <td colSpan="5">No leave requests yet</td>
                  </tr>
                ) : (
                  recentRequests.map(req => (
                    <tr key={req.id}>

                      <td>{req.leaveType}</td>

                      <td>{req.startDate}</td>

                      <td>{req.endDate}</td>

                      <td>{req.numberOfDays}</td>

                      <td>
                        <span className={`status ${req.status.toLowerCase()}`}>
                          {req.status}
                        </span>
                      </td>

                    </tr>
                  ))
                )}

              </tbody>

            </table>

          </>
        )}

      </div>

    </MainLayout>
  );
};

export default EmployeeDashboard;