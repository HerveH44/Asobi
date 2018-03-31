import React from "react";
import { array, string, number, object, bool, func } from "prop-types";
import _ from "lodash";

import { withStyles } from 'material-ui/styles';
import Input, { InputLabel } from 'material-ui/Input';
import { FormControl, FormControlLabel, FormGroup, FormLabel } from 'material-ui/Form';
import { MenuItem } from 'material-ui/Menu';
import Select from 'material-ui/Select';
import Checkbox from 'material-ui/Checkbox';
import Radio, { RadioGroup } from 'material-ui/Radio';

import GameSettings from "./GameSettings";

const styles = theme => ({
  container: {
    display: 'flex',
    flexWrap: 'wrap',
    margin: 'auto',
    width: '900px',
    padding: '10px'
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing.unit * 2,
  },
  group: {
    margin: `${theme.spacing.unit}px 0`,
  },
});

class CreatePanel extends React.Component  {
    onChangeGameType = (event, gameType) => {
      this.props.editGame({ gameType });
    }

    handleCheck = name => event => {
      this.props.editGame({
        [name]: event.target.checked
      });
    }

    onChangeTitle = ({currentTarget: { value: title }}) => {
      this.props.editGame({ title });
    }

    onChangeSeats = ({currentTarget: { value: seats }}) => {
      this.props.editGame({ seats });
    }

    render() {
      const { availableSets, createGame, sets, editGame, title, seats, isPrivate, gameTypes, gameType, gameModes, gameMode, classes } = this.props;

      return(
        <FormControl className={classes.container} component='fieldset'>
          <FormLabel component='legend'>Create a game</FormLabel>
          <FormGroup row>
            <FormControl className={classes.formControl}>
              <InputLabel htmlFor="name-simple">Title</InputLabel>
              <Input value={title} onChange={this.onChangeTitle} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <InputLabel htmlFor="age-simple">Players</InputLabel>
              <Select value={seats} onChange={this.onChangeSeats}  inputProps={{
                name: 'age',
                id: 'age-simple',
              }}>
                {_.range(2, 10).map(x =>
                  <MenuItem key={_.uniqueId()} value={x}>{x}</MenuItem>)}
              </Select>
            </FormControl>
            <FormControl className={classes.formControl}>
              <FormLabel>Private Game</FormLabel>
                <Checkbox
                  checked={isPrivate}
                  onChange={this.handleCheck('isPrivate')}
                />
            </FormControl>
            <FormControl className={classes.formControl}>
              <FormLabel >Game Type</FormLabel>
              <RadioGroup
                aria-label="gender"
                name="gender1"
                className={classes.group}
                value={gameType}
                onChange={this.onChangeGameType}
                row
              >
                {gameTypes.map(val =>
                  <FormControlLabel key={_.uniqueId()} value={val} control={<Radio />} label={val} />
                )}
              </RadioGroup>
            </FormControl>
          </FormGroup>
          <GameSettings
            gameMode={gameMode}
            gameModes={gameModes}
            editGame={editGame}
            sets={sets[gameType]}
            availableSets={availableSets}/>
            <button onClick={createGame}>
                Create game
            </button>
        </FormControl>
    )
  }
}

CreatePanel.propTypes = {
  title: string.isRequired,
  seats: number.isRequired,
  isPrivate: bool.isRequired,
  gameTypes: array.isRequired,
  gameType: string.isRequired,
  classes: object.isRequired,
  editGame: func.isRequired,
  gameModes: array.isRequired,
  gameMode: string.isRequired,
  sets: object.isRequired,
  availableSets: object.isRequired,
  createGame: func.isRequired
}

export default withStyles(styles)(CreatePanel);
