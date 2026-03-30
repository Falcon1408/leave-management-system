import { Routes, Route } from "react-router-dom";

import Login from "./pages/auth/Login";

/* Employee Pages */
import EmployeeDashboard from "./pages/employee/EmployeeDashboard";
import ApplyLeavePage from "./pages/employee/ApplyLeavePage";
import LeaveHistoryPage from "./pages/employee/LeaveHistoryPage";

/* Manager Pages */
import ManagerDashboard from "./pages/manager/ManagerDashboard";
import LeaveApprovalPage from "./pages/manager/LeaveApprovalPage";
import TeamLeaveHistoryPage from "./pages/manager/TeamLeaveHistoryPage";

/* Admin Pages */
import AdminDashboard from "./pages/admin/AdminDashboard";
import EmployeesPage from "./pages/admin/EmployeesPage";
import LeaveTypesPage from "./pages/admin/LeaveTypesPage";
import LeaveBalancesPage from "./pages/admin/LeaveBalancesPage";
import HolidaysPage from "./pages/admin/HolidaysPage";

/* Auth */
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>

      {/* Public */}
      <Route path="/" element={<Login />} />

      {/* EMPLOYEE */}
      <Route element={<ProtectedRoute allowedRole="EMPLOYEE" />}>
        <Route path="/employee" element={<EmployeeDashboard />} />
        <Route path="/employee/apply" element={<ApplyLeavePage />} />
        <Route path="/employee/history" element={<LeaveHistoryPage />} />
      </Route>

      {/* MANAGER */}
      <Route element={<ProtectedRoute allowedRole="MANAGER" />}>
        <Route path="/manager" element={<ManagerDashboard />} />
        <Route path="/manager/leave-approvals" element={<LeaveApprovalPage />} />
        <Route path="/manager/team-history" element={<TeamLeaveHistoryPage />} />
      </Route>

      {/* ADMIN */}
      <Route element={<ProtectedRoute allowedRole="ADMIN" />}>
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/admin/employees" element={<EmployeesPage />} />
        <Route path="/admin/leave-types" element={<LeaveTypesPage />} />
        <Route path="/admin/leave-balances" element={<LeaveBalancesPage />} />
        <Route path="/admin/holidays" element={<HolidaysPage />} />
      </Route>

    </Routes>
  );
}

export default App;