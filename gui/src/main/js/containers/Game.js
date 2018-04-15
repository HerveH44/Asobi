import React, {Component, Fragment} from "react";
import {func, string} from "prop-types";
import {connect} from 'react-redux';
import {joinGame, leaveGame, startGame, editGameSettings} from "../actions/server"
import StartPanel from "../components/StartPanel"

class Game extends Component {
    componentDidMount() {
        const {joinGame, gameId} = this.props;
        joinGame(gameId);
    }

    componentWillUnmount() {
        const {leaveGame, gameId} = this.props;
        leaveGame(gameId);
    }

    render() {
        const {gameId, ...rest} = this.props;
        console.log(rest);
        return (
            <Fragment>
                <header>
                    <h1>We are in game {gameId}!</h1>
                </header>
                <div className='game-controls'>
                <div className='game-status'>
                    {/* <PlayersPanel/> */}
                    <StartPanel {...rest}/>
                </div>
                {/* <DeckSettings /> */}
                {/* <GameSettings/> */}
            </div>
          </Fragment>
        )
    }
}

Game.propTypes = {
    gameId: string.isRequired,
    joinGame: func.isRequired,
    leaveGame: func.isRequired
};

const mapStateToProps = ({
    gameSettings
}, {match: {params: {gameId}}}) => {
    return {
        ...gameSettings,
        gameId
    }
};

const mapDispatchToProps = {
    leaveGame,
    joinGame,
    startGame,
    editGameSettings
};

export default connect(mapStateToProps, mapDispatchToProps)(Game);
