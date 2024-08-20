import '../CSS/Form.css';
import aboutImage from '../Image/about.jpg';
import Footer from './footer';
import Navigation from "./navigation";


const AboutABC = () => {
    return(
        <>
        <Navigation/>


        <div className="about-abc-container">
            <h1 className="about-abc-heading">About ABC</h1>
            <div className="about-abc-content-container">
                <div className="about-abc-content">
                    <p>
                    ABC Restaurant is a premier dining destination located in the heart of Sri Lanka. Known for its exquisite cuisine and exceptional
                     service, ABC Restaurant offers a unique culinary experience that blends traditional Sri Lankan flavors with contemporary twists. 
                     Our chefs use only the freshest ingredients to create mouth-watering dishes that will leave you coming back for more. Whether 
                     you're looking for a place to enjoy a romantic dinner, a family gathering, or a business lunch, ABC Restaurant is the perfect 
                     choice. Come and experience the best of Sri Lankan hospitality and gastronomy at ABC Restaurant.</p>
                    <p>
                    Our vision is to be the leading restaurant chain in Sri Lanka, recognized for our innovative culinary creations, exceptional service, and commitment to sustainability. We aim to create memorable dining experiences that celebrate the richness of Sri Lankan culture and cuisine, while continuously evolving to meet the dynamic needs of our customers and communities.
                    </p>
                    <p>
                    At ABC Restaurant, our mission is to deliver unparalleled dining experiences by offering a diverse menu that combines traditional Sri Lankan flavors with contemporary twists, crafted from the freshest and highest quality ingredients. We are dedicated to providing exceptional service that exceeds our customers' expectations and fosters lasting relationships. Embracing innovation and technology, we enhance our operations to ensure convenience and efficiency for our customers and staff. We promote sustainability and ethical practices in all aspects of our business, from sourcing ingredients to managing our environmental footprint, and strive to create a welcoming and inclusive atmosphere where every guest feels valued and appreciated.
                    </p>
                </div>
                <div className="about-abc-image">
                    <img src={aboutImage} alt="ABC Restaurant" />
                </div>
            </div>
        </div>

        <div className="card-container">
                <div className="card text-bg-dark">
                    <div className="card-icon">
                        <i className="bi bi-building"></i>
                    </div>
                    <div className="card-text">
                        <p>10+ Branches</p>
                    </div>
                </div>
                <div className="card text-bg-dark">
                    <div className="card-icon">
                        <i className="bi bi-people-fill"></i>
                    </div>
                    <div className="card-text">
                        <p>500+ Employees</p>
                    </div>
                </div>
                <div className="card text-bg-dark">
                    <div className="card-icon">
                        <i className="bi bi-basket-fill"></i>
                    </div>
                    <div className="card-text">
                        <p>100% Fresh Ingredients</p>
                    </div>
                </div>
            </div>


            <Footer/>
        </>
    );
}
export default AboutABC;