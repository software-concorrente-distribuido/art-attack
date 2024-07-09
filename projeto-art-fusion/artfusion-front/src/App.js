import React from 'react';
import GlobalStyle from './styles/global';
import RoutesApp from './routes';
import { AuthProvider } from './contexts/auth';
import { Provider } from 'react-redux';
import { store } from './store/store';

const App = () => {
    return (
        <>
            <Provider store={store}>
                <AuthProvider>
                    <RoutesApp />
                    <GlobalStyle />
                </AuthProvider>
            </Provider>
        </>
    );
};

export default App;
