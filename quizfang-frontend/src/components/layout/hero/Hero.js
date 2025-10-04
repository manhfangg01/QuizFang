import React from "react";
import Slider from "react-slick";
import image1 from "../../../assets/images/ielts.jpg";
import image2 from "../../../assets/images/ielts-2.png";
import image3 from "../../../assets/images/hero2.png";
import platinum from "../../../assets/images/platinum.svg";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import "./Hero.css";

const Hero = () => {
  const slides = [
    {
      title: (
        <>
          Chào mừng đến với hệ thống luyện tập <strong>IELTS</strong>
        </>
      ),
      description: "Chúng tôi luôn cập nhật những đề thi mới nhất !",
      image: image1,
    },
    {
      title: (
        <div style={{ display: "inline", fontSize: "3.3rem" }}>
          Bạn có muốn biết <strong style={{ color: "#294563 " }}>điểm IELTS của mình</strong> ?
        </div>
      ),
      description: (
        <>
          Thi thử <strong style={{ color: "red" }}>miễn phí</strong> và biết điểm ngay tức thì, không cần đi lại, tất cả đều là trực tuyến
        </>
      ),
      image: image2,
    },
    {
      title: (
        <p style={{ fontSize: "2.5rem" }}>
          Tự hào là
          <span
            style={{
              color: "#493c3cff",
              display: "block",
              marginBottom: 0,
              textTransform: "uppercase",
              fontFamily: '"Montserrat", Helvetica, Arial, sans-serif',
              fontWeight: 800,
              fontSize: "3.5rem",
              lineHeight: 1.2,
            }}
          >
            Thành viên
          </span>
          <img src={platinum} alt="Bạch kim"></img>
          của <strong style={{ color: "red", display: "block", fontSize: "3.5rem" }}>Chương trình Đối tác Hội đồng Anh</strong>
        </p>
      ),
      description: "",
      image: image3,
    },
  ];
  const settings = {
    dots: true, // bullet
    infinite: true,
    speed: 600,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true, // tự động chạy
    autoplaySpeed: 5000,
  };

  const buttonStyles = {
    background: "#F9A95A",
    borderRadius: "38px",
    minWidth: "200px",
    fontWeight: "700",
    fontSize: "1rem",
    color: "#294563",
    textTransform: "uppercase",
    cursor: "pointer",
    padding: "10px",
  };
  return (
    <div className="hero-slider">
      <Slider {...settings}>
        {slides.map((slide, index) => (
          <div className="slide" key={index}>
            <div className="slide-content">
              <div className="text">
                <h1>{slide.title}</h1>
                <p>{slide.description}</p>
                <div style={{ display: "flex", justifyContent: "center", marginTop: "30px" }}>{index === 1 ? <button style={buttonStyles}>Bắt đầu thôi</button> : <></>}</div>
              </div>
              <div className="image">
                <img src={slide.image} alt={slide.title} />
              </div>
            </div>
          </div>
        ))}
      </Slider>
    </div>
  );
};

export default Hero;
