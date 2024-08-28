import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import '../CSS/Footer.css';

const SecFooter = () => {
    

    return (
            <div className="footer-copyright">
                &copy; {new Date().getFullYear()} ABC Restaurant. All Rights Reserved.
            </div>
     
    );
};

export default SecFooter;

