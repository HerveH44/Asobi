import React from "react";
import {func, string, bool, array} from "prop-types";
import {Checkbox, Select} from "../../utils";
import {editGameSettings, joinGame, leaveGame, startGame} from "../../../actions/server";
import {connect} from "react-redux";
import {onEditStartPanel} from "../../../actions/game";

const StartPanel = ({gameType, packsInfo, isHost, didGameStart, ...rest}) => (
    <fieldset className='start-controls fieldset'>
        <legend className='legend game-legend'>Game</legend>
        <span>
        <div>Type: {gameType}</div>
        <div>Infos: {packsInfo}</div>
            {(isHost && !didGameStart)
                ? <StartControls gameType={gameType} {...rest} />
                : <div/>}
        </span>
    </fieldset>
);

StartPanel.propTypes = {
    gameType: string.isRequired,
    packsInfo: string.isRequired,
    isHost: bool.isRequired,
    didGameStart: bool.isRequired
};

const StartControls = ({startGame, ...rest}) => (
    <div>
        <Options {...rest}/>
        <div>
            <button onClick={startGame}>Start game</button>
        </div>
    </div>
);


StartControls.propTypes = {
    startGame: func.isRequired
};

const Options = ({gameType, useTimer, timers, timerLength, addBots, shufflePlayers, onEditStartPanel}) => {
    const handleChangeChecked = name => (event) => {
        onEditStartPanel({[name]: event.target.checked});
    };

    const handleChange = name => (event) => {
        onEditStartPanel({[name]: event.target.value});
    };

    return (
        <span>
            <Checkbox
                checked={addBots}
                onChange={handleChangeChecked('addBots')}
                text=" Add Bots"
            />
            {gameType === "DRAFT"
                ? <div>

                    <Checkbox
                        checked={useTimer}
                        onChange={handleChangeChecked('useTimer')}
                        text=" Timer: "
                    />
                    <Select
                        value={timerLength}
                        opts={timers}
                        onChange={handleChange("timerLength")}
                        disabled={!useTimer}/>
                </div>
                : <div/>
            }

            <Checkbox
                checked={shufflePlayers}
                onChange={handleChangeChecked('shufflePlayers')}
                text=" Random Seating"/>
        </span>
    );
};

Options.propTypes = {
    useTimer: bool.isRequired,
    addBots: bool.isRequired,
    shufflePlayers: bool.isRequired,
    onEditStartPanel: func.isRequired,
    timers: array.isRequired,
    timerLength: string.isRequired,
    gameType: string.isRequired
};

const mapStateToProps = ({gameState, startPanel}) => ({
    ...gameState,
    ...startPanel
});

const mapDispatchToProps = {
    leaveGame,
    joinGame,
    startGame,
    onEditStartPanel
};

export default connect(mapStateToProps, mapDispatchToProps)(StartPanel);

