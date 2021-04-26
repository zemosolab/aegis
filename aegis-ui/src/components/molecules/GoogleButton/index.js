import React from 'react';
import { googleAuthenticate } from '../../../services/AuthService';
import '../../../assets/css/google.css';
import GoogleIcon from '../../atoms/GoogleIcon';

const GoogleButton = () => {
  const login = () => {
    googleAuthenticate();
  }

  return (<div>
    <button type="button" class="google-button" onClick={login}>
      <GoogleIcon />
      <span class="google-button__text">Sign in with Google</span>
    </button>
  </div>);
}

export default GoogleButton;