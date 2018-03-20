import React from "react";
import {connect} from "react-redux";
import { sendData } from "../../reducers/websocket";
import { editDefault } from "../../reducers/default";
import _ from "lodash";

import { withStyles } from 'material-ui/styles';
import Input, { InputLabel } from 'material-ui/Input';
import { FormControl, FormControlLabel, FormGroup, FormLabel } from 'material-ui/Form';
import { MenuItem } from 'material-ui/Menu';
import Select from 'material-ui/Select';
import Checkbox from 'material-ui/Checkbox';
import Radio, { RadioGroup } from 'material-ui/Radio';

// import GameOptions from "./GameOptions";

const styles = theme => ({
  container: {
    display: 'flex',
    flexWrap: 'wrap',
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
  state = {
    name: "Create Game",
    seat: 2,
    age: "",
    isPrivate: false,
    gameType: "draft"
  }

    handleRadio = (event, gameType) => {
      this.setState({ gameType });
    }

    handleCheck = name => event => {
      this.setState({ [name]: event.target.checked });
    }

    onCreateGame() {
      console.log(this);
      sendData();
    }

    onChangeTitle({currentTarget: { value: gameTitle }}) {
      editDefault({
        gameTitle
      })
    }

    onChangeSeats({currentTarget: { value: seats }}) {
      editDefault({
        seats
      })
    }

    render() {
      const { title, seats, classes} = this.props;

      return(
        <FormControl component='fieldset'>
          <FormLabel component='legend'>Create a game</FormLabel>
          <FormGroup row>
            <FormControl className={classes.formControl}>
              <InputLabel htmlFor="name-simple">Title</InputLabel>
              <Input value={title} onChange={this.onChangeTitle} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <InputLabel htmlFor="age-simple">Players</InputLabel>
              <Select value={10} onChange={this.onChangeSeats}  inputProps={{
                name: 'age',
                id: 'age-simple',
              }}>
                {_.range(2, 100).map(x =>
                  <MenuItem key={_.uniqueId()} value={x}>{x}</MenuItem>)}
              </Select>
            </FormControl>
            <FormControlLabel
              control={
                <Checkbox
                  checked={this.state.isPrivate}
                  onChange={this.handleCheck('isPrivate')}
                  // value={this.state.isPrivate}
                />}
              label="Private Game"
            />
          </FormGroup>
          <FormGroup>
            <FormControl component="fieldset" required className={classes.formControl}>
              <FormLabel component="legend">Game Type</FormLabel>
              <RadioGroup
                aria-label="gender"
                name="gender1"
                className={classes.group}
                value={this.state.gameType}
                onChange={this.handleRadio}
                row
              >
                {/* TODO: Mettre les variables dans une config */}
                {["draft", "sealed", "cube draft", "cube sealed", "chaos"].map(val =>
                  <FormControlLabel key={_.uniqueId()} value={val} control={<Radio />} label={val} />
                )}
              </RadioGroup>
            </FormControl>
          </FormGroup>
          {/*<GameOptions/>*/}
          <p>
              <button onClick={this.onCreateGame}>
                  Create game
              </button>
          </p>
        </FormControl>
    )
  }
}

const mapStateToProps= ({def}) => ({
  ...def
});

const mapDispatchToProps = {
  sendData,
  editDefault
};

export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(CreatePanel));
