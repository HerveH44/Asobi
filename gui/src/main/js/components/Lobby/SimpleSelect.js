import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const styles = theme => ({
    root: {
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
});

class SimpleSelect extends React.Component {
    state = {
        players: 2,
        packs: 3
    };

    handleChange = event => {
        this.setState({ [event.target.name]: event.target.value });
    };

    render() {
        const { classes } = this.props;

        return (
            <form className={classes.root} autoComplete="off">
                <FormControl required className={classes.formControl}>
                    <InputLabel htmlFor="age-required">Players</InputLabel>
                    <Select
                        value={this.state.players}
                        onChange={this.handleChange}
                        name="players"
                        inputProps={{
                            id: 'age-required',
                        }}
                        className={classes.selectEmpty}
                    >
                        <MenuItem value="">
                            <em>None</em>
                        </MenuItem>
                        <MenuItem value={2}>Two</MenuItem>
                        <MenuItem value={3}>Three</MenuItem>
                        <MenuItem value={4}>Four</MenuItem>
                    </Select>
                    <FormHelperText>Required</FormHelperText>
                </FormControl>
                <FormControl required className={classes.formControl}>
                    <InputLabel htmlFor="age-required">Packs</InputLabel>
                    <Select
                        value={this.state.packs}
                        onChange={this.handleChange}
                        name="packs"
                        inputProps={{
                            id: 'age-required',
                        }}
                        className={classes.selectEmpty}
                    >
                        <MenuItem value="">
                            <em>None</em>
                        </MenuItem>
                        <MenuItem value={3}>Three</MenuItem>
                        <MenuItem value={4}>Four</MenuItem>
                        <MenuItem value={5}>Five</MenuItem>
                    </Select>
                    <FormHelperText>Required</FormHelperText>
                </FormControl>
            </form>
        );
    }
}

SimpleSelect.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SimpleSelect);
