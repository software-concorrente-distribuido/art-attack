import { Fragment } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from '../pages/Home';
import Register from '../pages/Register';
import Login from '../pages/Login';
import useAuth from '../hooks/useAuth';
import Paint from '../pages/Paint';

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
                        element={<Home/>} // deixar a rota home livre para implementação 
                        // element={<Private Item={Home} />} -> Guardar rotas
                    />
                    <Route path="/login" element={<Login />} />
                    <Route exact path="/register" element={<Register />} />
                    <Route path="*" element={<Login />} />
                    <Route path="/paint"
                        element={<Paint />} />
                </Routes>
            </Fragment>
        </BrowserRouter>
    );
};

export default RoutesApp;
