import React, {Component, Fragment} from "react";
import {func, string} from "prop-types";
import {connect} from 'react-redux';
import {joinGame, leaveGame, startGame, editGameSettings} from "../actions/server"
import StartPanel from "../components/StartPanel"
import PlayersPanel from "../components/PlayersPanel"
import DeckSettings from "../components/DeckSettings"
import CardsZone from "../components/CardsZone"
import GameSettings from "../components/GameSettings";

class Game extends Component {
    componentDidMount() {
        const {joinGame, editGameSettings, gameId} = this.props;
        editGameSettings({gameId});
        joinGame(gameId);
    }

    componentWillUnmount() {
        const {leaveGame, gameId} = this.props;
        leaveGame(gameId);
    }

    render() {
        return (
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
        )
    }
}

Game.propTypes = {
    gameId: string.isRequired,
    joinGame: func.isRequired,
    leaveGame: func.isRequired,
    editGameSettings: func.isRequired
};

const mapStateToProps = ({}, {match: {params: {gameId}}}) => ({
    gameId
});

const mapDispatchToProps = {
    leaveGame,
    joinGame,
    startGame,
    editGameSettings
};

export default connect(mapStateToProps, mapDispatchToProps)(Game);
