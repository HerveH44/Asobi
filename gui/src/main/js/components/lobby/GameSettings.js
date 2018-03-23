import React, { Fragment } from 'react';
import { func, array, string, object } from 'prop-types';
import { withStyles } from 'material-ui/styles';
import Paper from 'material-ui/Paper';
import Tabs, { Tab } from 'material-ui/Tabs';
import Grid from "material-ui/Grid";
import Button from 'material-ui/Button';
import AddIcon from 'material-ui-icons/Add';
import TextField from 'material-ui/TextField';
import _ from "lodash";

const styles = theme => ({
  root: {
    flexGrow: 1,
    marginTop: theme.spacing.unit * 3,
  },
  list: {
    padding: 20,
    marginTop: 10,
    marginBottom: 10
  },
  button: {
    margin: theme.spacing.unit,
  },
});

class CenteredTabs extends React.Component {

  handleChange = (event, gameMode) => {
    this.props.editGame({ gameMode })
  };

  render() {
    const { classes, gameMode, gameModes } = this.props;

    return (
      <Paper className={classes.root}>
        <Tabs
          value={gameMode}
          onChange={this.handleChange}
          indicatorColor="primary"
          textColor="primary"
          centered
        >
          {gameModes.map(mode =>(
            <Tab key={_.uniqueId()} value={mode} label={mode} />
          ))}
        </Tabs>
        {gameMode == "normal" && <NormalGameSettings classes={classes}/>}
      </Paper>
    );
  }
}

CenteredTabs.propTypes = {
  classes: object.isRequired,
  gameModes: array.isRequired,
  gameMode: string.isRequired,
  editGame: func.isRequired
};

const NormalGameSettings = ({ classes }) => (
  <Grid item sm>
    <Paper className={classes.list}>
      <Set classes={classes} />
    </Paper>
  </Grid>
)

NormalGameSettings.propTypes = {
  classes: object.isRequired
};

const Set = ({ classes }) => (
  <Fragment>
    <TextField
      id="select-currency-native"
      select
      label="Set"
      value={1}
      SelectProps={{
        native: true
      }}
      helperText="Please select a set"
      margin="normal"
    >
      <optgroup label="group">
        <option>toto</option>
        <option>toto2</option>
        <option>toto3</option>
        <option>toto4</option>
      </optgroup>
    </TextField>
    <Button
      mini
      variant="fab"
      color="primary"
      aria-label="add"
      className={classes.button}>
      <AddIcon />
    </Button>
  </Fragment>
)

Set.propTypes = {
  classes: object.isRequired
};

export default withStyles(styles)(CenteredTabs);
