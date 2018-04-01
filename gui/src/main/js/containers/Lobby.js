import React, {Fragment} from "react";
import {connect} from 'react-redux';
import {editGame, CREATE_GAME} from "../reducers/game";
import {object, func} from "prop-types";

import Header from "../components/Header";
import JoinPanel from "../components/JoinPanel";
import NewsPanel from "../components/NewsPanel";
import Footer from "../components/Footer";
import CreatePanel from "../components/CreateGamePanel";

const Lobby = ({
    site,
    game,
    ...rest
}) => (
    <Fragment>
        <Header {...site}/>
        <CreatePanel {...game} {...rest}/>
        <JoinPanel {...site}/>
        <NewsPanel {...site}/>
        <Footer {...site}/>
    </Fragment>
);

Lobby.propTypes = {
    site: object.isRequired,
    game: object.isRequired,
    editGame: func.isRequired,
    createGame: func.isRequired
}

const mapStateToProps = (state) => (state);
const mapDispatchToProps = {
    editGame,
    createGame: CREATE_GAME
};

export default connect(mapStateToProps, mapDispatchToProps)(Lobby);
