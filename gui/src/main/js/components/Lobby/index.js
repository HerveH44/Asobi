import React from "react";
import {connect} from 'react-redux';
import {func, object} from "prop-types";

import Header from "./Header/index";
import JoinPanel from "./JoinPanel/index";
import NewsPanel from "./NewsPanel/index";
import Footer from "./Footer/index";
import CreatePanel from "./CreateGamePanel/index";
import {CREATE_GAME, editPackNumber, editGame} from "../../actions/game";

const Lobby = ({
    site,
    game,
    ...rest
}) => (
    <div className="container">
        <div className="lobby">
            <Header {...site}/>
            <CreatePanel {...game} {...rest}/>
            <JoinPanel {...site}/>
            <NewsPanel {...site}/>
            <Footer {...site}/>
        </div>
    </div>
);

Lobby.propTypes = {
    site: object.isRequired,
    game: object.isRequired,
    editGame: func.isRequired,
    createGame: func.isRequired
};

const mapStateToProps = (state) => (state);
const mapDispatchToProps = {
    editGame,
    createGame: CREATE_GAME,
    EDIT_PACK_NUMBER: editPackNumber
};

export default connect(mapStateToProps, mapDispatchToProps)(Lobby);
