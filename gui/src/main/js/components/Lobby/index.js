import React from "react";
import {connect} from 'react-redux';
import withStyles from '@material-ui/core/styles/withStyles';
import {editPackNumber, editGame} from "../../actions/game";
import {createGame} from "../../actions/server";
import {func, object} from "prop-types";
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

import Header from "./Header/index";
import JoinPanel from "./JoinPanel/index";
import NewsPanel from "./NewsPanel/index";
import Footer from "./Footer/index";
import CreatePanel from "./CreateGamePanel/index";
import CenteredTabs from "./CenteredTabs";
import SimpleSelect from "./SimpleSelect";
/*
const Lobby = ({
    site,
    game,
    ...rest
}) => (
    <div className="container">
        <div className="lobby">
            <Header {...site}/>
            <CreatePanel {...game} {...rest}/>
            <JoinPanel {...site}/>
            <NewsPanel {...site}/>
            <Footer {...site}/>
        </div>
    </div>
*/

const styles = theme => ({
    layout: {
        width: 'auto',
        marginLeft: theme.spacing.unit * 2,
        marginRight: theme.spacing.unit * 2,
        [theme.breakpoints.up(600 + theme.spacing.unit * 2 * 2)]: {
            width: 600,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        marginTop: theme.spacing.unit * 3,
        marginBottom: theme.spacing.unit * 3,
        padding: theme.spacing.unit * 2,
        [theme.breakpoints.up(600 + theme.spacing.unit * 3 * 2)]: {
            marginTop: theme.spacing.unit * 6,
            marginBottom: theme.spacing.unit * 6,
            padding: theme.spacing.unit * 3,
        },
    },
    stepper: {
        padding: `${theme.spacing.unit * 3}px 0 ${theme.spacing.unit * 5}px`,
    },
    buttons: {
        display: 'flex',
        justifyContent: 'flex-end',
    },
    button: {
        marginTop: theme.spacing.unit * 3,
        marginLeft: theme.spacing.unit,
    },
});

const Lobby = ({classes, site, game, ...rest }) => (
    <main className={classes.layout}>
        <Paper className={classes.paper}>
            <Typography variant="display1" align="center">
                Asobi
            </Typography>
            <React.Fragment>
                <React.Fragment>
                    <Typography variant="title" gutterBottom>
                        Create Game
                    </Typography>
                    <Grid container spacing={24}>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                required
                                id="title"
                                name="title"
                                label="Game Title"
                                fullWidth
                                autoComplete="fname"
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <FormControlLabel
                                control={<Checkbox color="secondary" name="isPrivate" value="yes" />}
                                label="Keep room private"
                            />
                        </Grid>
                        <Grid item xs={24}>
                            <SimpleSelect/>
                        </Grid>
                        <Grid item xs={24}>
                            <CenteredTabs/>
                        </Grid>
                    </Grid>
                </React.Fragment>
                <React.Fragment>
                    <div className={classes.buttons}>
                        <Button
                            variant="contained"
                            color="primary"
                            className={classes.button}>
                            Create
                        </Button>
                    </div>
                </React.Fragment>
            </React.Fragment>
        </Paper>
    </main>
);

Lobby.propTypes = {
    site: object.isRequired,
    game: object.isRequired,
    editGame: func.isRequired,
    createGame: func.isRequired,
    classes: object.isRequired
};

const mapStateToProps = (state) => (state);
const mapDispatchToProps = {
    editGame,
    createGame,
    editPackNumber
};

export default connect(mapStateToProps, mapDispatchToProps)(withStyles(styles)(Lobby));
