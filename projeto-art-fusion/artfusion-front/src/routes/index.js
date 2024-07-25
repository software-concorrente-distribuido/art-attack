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
                        path="/home"
                        element={<HomeTempo />} // deixar a rota home livre para implementação
                        // element={<Private Item={HomeTempo} />}
                    />
                    <Route path="/login" element={<Login />} />
                    <Route exact path="/register" element={<Register />} />

                    <Route exact path="/minhas-artes" element={<MyArts />} />
                    <Route
                        exact
                        path="/artes-compartilhadas"
                        element={<SharedArts />}
                    />

                    <Route
                        exact
                        path="/artes-recentes-preview"
                        element={<RecentArtsPreview />}
                    />

                    <Route
                        exact
                        path="/artes-recentes-contribution"
                        element={<RecentArtsContribution />}
                    />

                    <Route
                        exact
                        path="/perfil"
                        element={<EditProfileModal />}
                    />

                    <Route
                        exact
                        path="/ShareModal"
                        element={<ShareModal />}
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
