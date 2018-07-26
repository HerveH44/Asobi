import React from "react";
import {func, string, bool, array} from "prop-types";
import { Checkbox, Select } from "../utils";
import {editGameSettings, joinGame, leaveGame, startGame} from "../../actions/server";
import {connect} from "react-redux";

const StartPanel = ({type, packsInfo, isHost, didGameStart, ...rest}) => (
    <fieldset className='start-controls fieldset'>
        <legend className='legend game-legend'>Game</legend>
        <span>
        <div>Type: {type}</div>
        <div>Infos: {packsInfo}</div>
        {(isHost && !didGameStart)
            ? <StartControls type={type} {...rest} />
            : <div />}
        </span>
    </fieldset>
);

StartPanel.propTypes = {
    type: string.isRequired,
    packsInfo: string.isRequired,
    isHost: bool.isRequired,
    didGameStart: bool.isRequired
};

const StartControls = ({type, startGame, ...rest}) => {
//   const isDraft = type !== "sealed" && type !== "cube sealed";
    const isDraft = true;

    return (
        <div>
            {isDraft
                ? <Options {...rest}/>
                : <div/>}
            <div>
                <button onClick={startGame}>Start game</button>
            </div>
        </div>
    );
};

StartControls.propTypes = {
    type: string.isRequired,
    useTimer: bool.isRequired,
    startGame: func.isRequired
};

const Options = ({useTimer, timers, timerLength, addBots, shufflePlayers, editGameSettings}) => {
    const handleChangeChecked = name => (event) => {
        editGameSettings({[name]: event.target.checked})
    };

    const handleChange = name => (event) => {
        editGameSettings({[name]: event.target.value})
    };

    return (
        <span>
            <Checkbox
                checked={addBots}
                onChange={handleChangeChecked('addBots')}
                text=" bots"
            />
            <div>
                <Checkbox
                    checked={useTimer}
                    onChange={ handleChangeChecked('useTimer')}
                    text=" timer: "
                />
                <Select
                    value={timerLength}
                    opts={timers}
                    onChange={handleChange("timerLength")}
                    disabled={!useTimer}/>
            </div>
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
    editGameSettings: func.isRequired,
    timers: array.isRequired,
    timerLength: string.isRequired
};

const mapStateToProps = ({gameState, gameSettings}) => ({
    ...gameState,
    ...gameSettings
});

const mapDispatchToProps = {
    leaveGame,
    joinGame,
    startGame,
    editGameSettings
};

export default connect(mapStateToProps, mapDispatchToProps)(StartPanel);

