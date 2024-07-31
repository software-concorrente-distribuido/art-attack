import { Fragment } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from '../pages/Home';
import Register from '../pages/Register';
import Login from '../pages/Login';
import useAuth from '../hooks/useAuth';
import Paint from '../pages/Paint';
import HomeTempo from '../pages/HomeTempo';
import SharedArts from '../pages/SharedArts';
import MyArts from '../pages/MyArts';
import RecentArtsPreview from '../pages/RecentArtsPreview';
import RecentArtsContribution from '../pages/RecentArtsPreview';
import EditProfileModal from '../modals/EditProfileModal';
import ShareModal from '../modals/ShareModal';

const Private = ({ Item }) => {
    const { signed } = useAuth();

    return signed > 0 ? <Item /> : <Login />;
};

const RoutesApp = () => {
    return (
        <BrowserRouter>
            <Fragment>
                <Routes>
                    <Route
                        exact
                        path="/home-tempo"
                        element={<Private Item={HomeTempo} />}
                    />
                    <Route path="/login" element={<Login />} />
                    <Route exact path="/register" element={<Register />} />

                    <Route 
                        exact 
                        path="/minhas-artes" 
                        element={<Private Item={MyArts} />}
                    />

                    <Route
                        exact
                        path="/artes-compartilhadas"
                        element={<Private Item={SharedArts} />}
                    />

                    <Route
                        exact
                        path="/artes-recentes"
                        element={<Private Item={RecentArtsPreview} />}
                    />

                    <Route
                        exact
                        path="/perfil"
                        element={<Private Item={EditProfileModal} />}
                    />

                    <Route path="*" element={<Login />} />
                    <Route
                        path="/paint/:arteId/:salaUUID"
                        element={<Private Item={Paint} />}
                    />
                </Routes>
            </Fragment>
        </BrowserRouter>
    );
};

export default RoutesApp;
