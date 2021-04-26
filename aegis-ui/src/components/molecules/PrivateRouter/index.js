import React from 'react';
import {Route,Redirect} from 'react-router-dom';
import {isValidUser} from '../../../services/AuthService';
import { useSelector, useDispatch } from "react-redux";
 const PrivateRoute = ({component: Component, ...rest}) => {
    let isLogged = useSelector(state => state.AuthReducer.isLogged);
    return (

        <Route {...rest} render={props => (
            isLogged ?
                <Component {...props} />
            : <Redirect to="/" />
        )} />
    );
};
export default PrivateRoute;