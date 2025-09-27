import { Link } from "react-router-dom";
import "./Header.scss";
import logo from "../../../assets/icons/logo.svg";
import logoMobile from "../../../assets/icons/logo_mobile.svg";
import { useEffect, useState } from "react";
const Header = () => {
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 968);
  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth <= 968);
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <>
      <header className="header">
        <div className="container">
          <div className="logo-container">
            <Link to={"/"} className="logo">
              <img src={isMobile ? logoMobile : logo} alt="Logo" />
            </Link>
          </div>
        </div>
      </header>
    </>
  );
};

export default Header;
