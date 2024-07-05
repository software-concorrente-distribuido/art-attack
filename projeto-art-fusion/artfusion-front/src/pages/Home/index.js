import React from 'react';
import * as C from './styles';
import SideBar from '../../components/SideBar'
import EnableEditProfileModal from '../../modals/EnableEditProfileModal'


const Home = () => {
    return (
        <C.Container>
            <SideBar></SideBar>
            <EnableEditProfileModal></EnableEditProfileModal>
        </C.Container>
    );
};

export default Home;
