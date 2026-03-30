import MainLayout from "../../layout/MainLayout";
import { Link, Outlet } from "react-router-dom";
import "./AdminDashboard.css";

function AdminDashboard() {
  return (
    <MainLayout>
      <div className="admin-dashboard">

        <div className="dashboard-header">
          <h1>Admin Dashboard</h1>
          <p>Manage employees, leave types, balances and holidays.</p>
        </div>

        <div className="admin-modules">

          <Link to="/admin/employees" className="module-card">
            <h3>Employees</h3>
            <p>Create, update and manage employee accounts.</p>
          </Link>

          <Link to="/admin/leave-types" className="module-card">
            <h3>Leave Types</h3>
            <p>Configure leave categories and yearly limits.</p>
          </Link>

          <Link to="/admin/leave-balances" className="module-card">
            <h3>Leave Balances</h3>
            <p>View and manage employee leave allocations.</p>
          </Link>

          <Link to="/admin/holidays" className="module-card">
            <h3>Holidays</h3>
            <p>Configure company holidays and non-working days.</p>
          </Link>

        </div>

        <div className="system-info">
          <h2>System Information</h2>
          <ul>
            <li>Employee records are managed by HR.</li>
            <li>Leave types define annual leave limits.</li>
            <li>Holidays are excluded from leave calculations.</li>
            <li>Managers approve or reject employee leave requests.</li>
          </ul>
        </div>

        {/* Child pages will render here */}
        <Outlet />

      </div>
    </MainLayout>
  );
}

export default AdminDashboard;