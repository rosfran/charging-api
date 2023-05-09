import { Home } from "@mui/icons-material";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";
import InsertChartIcon from "@mui/icons-material/InsertChart";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";
import { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { DarkModeContext } from "../../context/darkModeContext";
import AuthService from "../../services/AuthService";
import "./sidebar.scss";

const Sidebar = () => {
  const { dispatch } = useContext(DarkModeContext);
  const navigate = useNavigate();
  const userRoles = AuthService.getCurrentUser()?.roles;
  const isAdmin = userRoles.includes("ROLE_ADMIN");

  return (
    <div className="sidebar">
      <div className="top">
        <Link to="/" style={{ textDecoration: "none" }}>
          <img className="companyLogo" />
        </Link>
      </div>
      <hr />
      <div className="center">
        <ul>
          <p className="title">MAIN</p>
          <Link to="/home" style={{ textDecoration: "none" }}>
            <li>
              <Home className="icon" />
              <span>Home</span>
            </li>
          </Link>
          {isAdmin && (
            <div>
              <p className="title">ADMIN</p>
              <Link to="/users" style={{ textDecoration: "none" }}>
                <li>
                  <PersonOutlineIcon className="icon" />
                  <span onClick={() => navigate("/users")}>Users</span>
                </li>
              </Link>
            </div>
          )}
          <p className="title">USER</p>
          <Link to="/solargrid" style={{ textDecoration: "none" }}>
            <li>
              <span>Solar Grids</span>
            </li>
          </Link>
          <Link
            to="/login"
            onClick={() => AuthService.logout()}
            style={{ textDecoration: "none" }}
          >
            <li>
              <ExitToAppIcon className="icon" />
              <span>Logout</span>
            </li>
          </Link>
        </ul>
      </div>
      <div className="bottom">
        <div
          className="colorOption"
          onClick={() => dispatch({ type: "LIGHT" })}
        ></div>
        <div
          className="colorOption"
          onClick={() => dispatch({ type: "DARK" })}
        ></div>
      </div>
    </div>
  );
};

export default Sidebar;
