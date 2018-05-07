import React from 'react';
import {func, array, string, object} from 'prop-types';
import {withStyles} from 'material-ui/styles';
import Paper from 'material-ui/Paper';
import Tabs, {Tab} from 'material-ui/Tabs';
import Grid from "material-ui/Grid";
import Button from 'material-ui/Button';
import AddIcon from 'material-ui-icons/Add';
import TextField from 'material-ui/TextField';
import _ from "lodash.uniqueid";

const styles = theme => ({
    root: {
        flexGrow: 1,
        marginTop: theme.spacing.unit * 3
    },
    list: {
        padding: 20,
        marginTop: 10,
        marginBottom: 10
    },
    button: {
        margin: theme.spacing.unit
    }
});

class CenteredTabs extends React.Component {

    handleChange = (event, gameMode) => {
        this
            .props
            .editGame({gameMode})
    };

    render() {
        const {availableSets, classes, gameMode, gameModes, sets} = this.props;

        return (
            <Paper className={classes.root}>
                <Tabs
                    value={gameMode}
                    onChange={this.handleChange}
                    indicatorColor="primary"
                    textColor="primary"
                    centered>
                    {gameModes.map(mode => (<Tab key={_.uniqueId()} value={mode} label={mode}/>))}
                </Tabs>
                {gameMode == "NORMAL" && <NormalGameSettings
                    availableSets={availableSets}
                    sets={sets}
                    classes={classes}/>}
            </Paper>
        );
    }
}

CenteredTabs.propTypes = {
    classes: object.isRequired,
    gameModes: array.isRequired,
    gameMode: string.isRequired,
    editGame: func.isRequired,
    sets: array.isRequired,
    availableSets: object.isRequired
};

const NormalGameSettings = ({availableSets, sets, classes}) => (
    <Grid item sm>
        <Paper className={classes.list}>
            {sets.map(set => (<Set
                key={_.uniqueId()}
                set={set}
                classes={classes}
                availableSets={availableSets}/>))}
            <Button
                mini
                variant="fab"
                color="primary"
                aria-label="add"
                className={classes.button}>
                <AddIcon/>
            </Button>
        </Paper>
    </Grid>
)

NormalGameSettings.propTypes = {
    classes: object.isRequired,
    sets: array.isRequired,
    availableSets: object.isRequired
};

const Set = ({availableSets, set, classes}) => (
    <TextField
        id="select-currency-native"
        select
        label="Set"
        value={1}
        SelectProps={{ native: true }}
        helperText="Please select a set"
        margin="normal">
        {Object
            .entries(availableSets)
            .map(([label, sets]) => {
                return (
                    <optgroup key={_.uniqueId()} label={label}>
                        {sets.map(({name}) => {
                            return <option key={_.uniqueId()}>{name}</option>
                        })}
                    </optgroup>
                )
            })}
    </TextField>
)

Set.propTypes = {
    classes: object.isRequired,
    set: object.isRequired,
    availableSets: object.isRequired
};

export default withStyles(styles)(CenteredTabs);
