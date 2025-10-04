import { useState } from "react";
import NavDropdown from "react-bootstrap/NavDropdown";

const HoverDropdown = ({ title, children }) => {
  const [show, setShow] = useState(false);

  return (
    <NavDropdown title={title} show={show} onMouseEnter={() => setShow(true)} onMouseLeave={() => setShow(false)} menuVariant="dark" className="menu-item">
      {children}
    </NavDropdown>
  );
};

export default HoverDropdown;
