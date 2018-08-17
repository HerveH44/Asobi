import React, {Component} from "react";
import {func, string} from "prop-types";
import {connect} from 'react-redux';
import {joinGame, leaveGame} from "../../actions/server"
import StartPanel from "./StartPanel/index"
import PlayersPanel from "./PlayersPanel/index"
import DeckSettings from "./DeckSettings/index"
import CardsZone from "./CardsZone/index"
import GameSettings from "./GameSettings/index";
import Chat from "./Chat";

class Game extends Component {
    componentDidMount() {
        const {joinGame, gameId} = this.props;
        joinGame(gameId);
        window.addEventListener('beforeunload', this.componentCleanup(this.props));
    }

    componentCleanup = ({leaveGame, gameId}) => () => { // this will hold the cleanup code
        leaveGame(gameId);
    };

    componentWillUnmount() {
        const {leaveGame, gameId} = this.props;
        leaveGame(gameId);
        window.removeEventListener('beforeunload', this.componentCleanup(this.props)); // remove the event handler for normal unmountin
    }

    render() {
        return (
            <div className={"container"}>
                <div className='game'>
                    <div className='game-controls'>
                        <div className='game-status'>
                            <PlayersPanel/>
                            <StartPanel/>
                        </div>
                        <DeckSettings/>
                        <GameSettings/>
                    </div>
                    <CardsZone/>
                </div>
                <Chat/>
            </div>
        )
    }
}

Game.propTypes = {
    gameId: string.isRequired,
    joinGame: func.isRequired,
    leaveGame: func.isRequired,
};

const mapStateToProps = ({}, {match: {params: {gameId}}}) => ({
    gameId
});

const mapDispatchToProps = {
    leaveGame,
    joinGame,
};

export default connect(mapStateToProps, mapDispatchToProps)(Game);
