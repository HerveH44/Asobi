import React from "react";
import {func, string, bool} from "prop-types";
import {FormControlLabel, FormControl } from "material-ui/Form";
import Checkbox from "material-ui/Checkbox"
import Select from "material-ui/Select"
import MenuItem from "material-ui/Menu/MenuItem"

const StartPanel = ({type, packsInfo, isHost, didGameStart, ...rest}) => (
  <fieldset className='start-controls fieldset'>
    <legend className='legend game-legend'>Game</legend>
    <span>
      <div>Type: {type}</div>
      <div>Infos: {packsInfo}</div>
      {/*(isHost && !didGameStart)*/ true
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
}

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
}

const Options = ({useTimer, addBots, shufflePlayers, editGameSettings}) => {
    const timers = ["Fast", "Moderate", "Slow", "Leisurely"];
    const handleChange = name => (event) => {
        editGameSettings({[name]: event.target.checked})
    };
  return (
    <span>
      {/* <Checkbox side="left" link="addBots" text=" bots"/> */}
      <FormControlLabel
          control={
            <Checkbox
              checked={addBots}
              onChange={handleChange('addBots')}
              value="addBots"
            />
          }
          label="bots"
        />
      <div>
        {/* <Checkbox side="left" link="useTimer" text=" timer: "/> */}
        <FormControlLabel
          control={
            <Checkbox
              checked={useTimer}
              onChange={ handleChange('useTimer')}
              value="useTimer"
            />
          }
          label="timer"
        />
        {/* <Select link="timerLength" opts={timers} disabled={!useTimer}/> */}
        <FormControl>
            <Select
              value={10}
            //   onChange={handleChange('timerLength')}
            >
                <MenuItem value={10}>Ten</MenuItem>
            </Select>
        </FormControl>
        {/* <Checkbox side="left" link="shufflePlayers" text=" Random Seating"/> */}
        <FormControlLabel
          control={
            <Checkbox
              checked={shufflePlayers}
              onChange={handleChange('shufflePlayers')}
              value="shufflePlayers"
            />
          }
          label="Random Seating"
        />
      </div>
    </span>
  );
};

Options.propTypes = {
    useTimer: bool.isRequired,
    addBots: bool.isRequired,
    shufflePlayers: bool.isRequired,
    editGameSettings: func.isRequired
}

export default StartPanel;
