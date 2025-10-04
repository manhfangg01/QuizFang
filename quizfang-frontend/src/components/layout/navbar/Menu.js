import "./Menu.scss";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import NavDropdown from "react-bootstrap/NavDropdown";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaHome, FaBell } from "react-icons/fa";
import { Link } from "react-router-dom";
import HoverDropdown from "./HoverDropdown";

const Menu = () => {
  return (
    <Navbar expand="lg" className="bg-body-tertiary menu">
      <Container className="menu-container">
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/" className="nav-logo">
              <FaHome />
            </Nav.Link>

            <HoverDropdown title="Thư viện đề thi IELTS" id="basic-nav-dropdown-types" className="menu-item">
              <NavDropdown.Item as={Link} to="/ielts-listening">
                IELTS Listening Test
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/ielts-reading">
                IELTS Reading Test
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/ielts-speaking">
                IELTS Speaking Test
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/ielts-writing">
                IELTS Writing Test
              </NavDropdown.Item>
            </HoverDropdown>

            <HoverDropdown title="IELTS Tips" id="basic-nav-dropdown-tips" className="menu-item">
              <NavDropdown.Item as={Link} to="/iot-news">
                IOT News
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/listening-tips">
                Listening Tips
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/reading-tips">
                Reading Tips
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/writing-tips">
                Writing Tips
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/speaking-tips">
                Speaking Tips
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/grammar">
                Grammar
              </NavDropdown.Item>
            </HoverDropdown>

            <HoverDropdown title="IELTS Preps" id="basic-nav-dropdown-prep" className="menu-item">
              <NavDropdown.Item as={Link} to="/listening-practice">
                Luyện thi Listening
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/reading-practice">
                Luyện thi Reading
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/writing-practice">
                Luyện thi Writing
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/speaking-practice">
                Luyện thi Speaking
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/consultation">
                Tư Vấn Thi
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/registration">
                Đăng kí thi
              </NavDropdown.Item>
            </HoverDropdown>

            <HoverDropdown title="Lớp học trực tuyến" id="basic-nav-dropdown-online-classes" className="menu-item">
              <NavDropdown.Item as={Link} to="/online-listening">
                Listening
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/online-reading">
                Reading
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/online-writing">
                Writing
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/online-speaking">
                Speaking
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/study-abroad">
                Du học
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/vocabulary">
                Vocabulary
              </NavDropdown.Item>
            </HoverDropdown>

            <HoverDropdown title="IELTS courses" id="basic-nav-dropdown-courses" className="menu-item">
              <NavDropdown.Item as={Link} to="/courses">
                Các khóa học
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/schedule">
                Lịch khai giảng
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/one-on-one">
                IELTS 1v1
              </NavDropdown.Item>
            </HoverDropdown>

            <HoverDropdown title="Du học Intergrate" id="basic-nav-dropdown-overseas" className="menu-item">
              <NavDropdown.Item as={Link} to="/integrate-study">
                Du học cùng Integrate
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/usa-study">
                Du học Mỹ
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/australia-study">
                Du học Úc
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/canada-study">
                Du học Canada
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/uk-study">
                Du học Anh
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/school-list">
                Danh sách trường
              </NavDropdown.Item>
            </HoverDropdown>
          </Nav>

          <Nav>
            <NavDropdown title={<FaBell />} id="basic-nav-dropdown-messages" className="no-caret nav-logo menu-item">
              <NavDropdown.Item as={Link} to="/notifications">
                Thông báo
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/messages">
                Tin nhắn
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item as={Link} to="/updates">
                Cập nhật
              </NavDropdown.Item>
            </NavDropdown>
            <Nav.Link as={Link} to="/login" className="menu-item">
              Đăng nhập
            </Nav.Link>
            <Nav.Link as={Link} to="/signup" className="menu-item">
              Đăng Kí
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Menu;
