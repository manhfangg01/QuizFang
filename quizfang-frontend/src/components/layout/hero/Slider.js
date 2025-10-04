import { useEffect, useState } from "react";
import { FaCaretLeft, FaCaretRight } from "react-icons/fa";
import { GoDotFill, GoDot } from "react-icons/go";

const Slider = ({ slides }) => {
  const [currentIndex, setCurrentIndex] = useState(0);

  const silderStyles = {
    height: "100%",
    position: "relative",
  };

  const slideStyles = {
    width: "100%",
    height: "100%",
    borderRadius: "10px",
    backgroundPosition: "center",
    backgroundSize: "cover",
    backgroundColor: "#294563 ",
    display: "flex",
  };

  const rightArrowStyles = {
    position: "absolute",
    top: "50%",
    transform: "translate(0,-50%)",
    right: "32px",
    fontSize: "45px",
    color: "#000",
    zIndex: 1,
    cursor: "pointer",
  };

  const leftArrowStyles = {
    position: "absolute",
    top: "50%",
    transform: "translate(0,-50%)",
    left: "32px",
    fontSize: "45px",
    color: "#000",
    zIndex: 1,
    cursor: "pointer",
  };

  const goToPrevious = () => {
    const isFirstSlide = currentIndex === 0;
    const newIndex = isFirstSlide ? slides.length - 1 : currentIndex - 1;
    setCurrentIndex(newIndex);
  };

  const goToNext = () => {
    const isLastSlide = currentIndex === slides.length - 1;
    const newIndex = isLastSlide ? 0 : currentIndex + 1;
    setCurrentIndex(newIndex);
  };

  const dotContainerStyles = {
    display: "flex",
    justifyContent: "center",
    margin: "0 auto",
    gap: "20px",
  };

  const contentStyles = {
    width: "50%",
    height: "90%",
  };
  const imageStyles = {
    width: "50%",
    height: "90%",
  };

  useEffect(() => {
    const interval = setInterval(() => {
      goToNext();
    }, 3000);
    return () => clearInterval(interval);
  }, [currentIndex]);

  return (
    <div style={silderStyles}>
      <div style={leftArrowStyles} onClick={goToPrevious}>
        <FaCaretLeft />
      </div>
      <div style={rightArrowStyles} onClick={goToNext}>
        <FaCaretRight />
      </div>
      <div style={slideStyles}>
        {slides.map((slide, slideIndex) => (
          <div key={slideIndex} style={{ display: slideIndex === currentIndex ? "flex" : "none", width: "100%" }}>
            <div style={contentStyles}>
              <h1>{slide.content}</h1>
            </div>
            <div style={imageStyles}>
              <img src={slide.image} alt="slide" style={{ width: "100%", height: "100%", objectFit: "cover", borderRadius: "10px" }} />
            </div>
          </div>
        ))}
      </div>

      <div style={dotContainerStyles}>
        {slides.map((slide, slideIndex) => (
          <div key={slideIndex}>{slideIndex === currentIndex ? <GoDot /> : <GoDotFill />} </div>
        ))}
      </div>
    </div>
  );
};
export default Slider;
