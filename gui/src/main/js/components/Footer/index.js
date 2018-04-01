import React from "react";
import {string, object} from "prop-types";

import {withStyles} from 'material-ui/styles';
import Paper from 'material-ui/Paper';
import Typography from 'material-ui/Typography';

const styles = theme => ({
    root: theme
        .mixins
        .gutters({
            paddingTop: 16,
            paddingBottom: 16,
            marginTop: theme.spacing.unit * 3
        })
});

const Footer = ({repo, version, classes}) => (
    <div>
        <Paper className={classes.root} elevation={4}>
            <Typography component="p">
                <strong>dr4ft</strong>{" "}
                is a fork of the{" "}
                <code>drafts.ninja</code>{" "}
                arxanas fork of the{" "}
                <code>draft</code>{" "}
                project by aeosynth. Contributions welcome!{" "}
                <a href={repo}>
                    <img
                        src='https://upload.wikimedia.org/wikipedia/commons/9/91/Octicons-mark-github.svg'
                        alt='GitHub'
                        title='GitHub Repository'
                        align='top'
                        height='18'/> {repo}
                </a>
            </Typography>
            <Typography component="p">
                Running Version{" "}
                <a href={`https://github.com/dr4fters/dr4ft/commit/${version}`}>
                    {version}
                </a>
            </Typography>
        </Paper>
        <p></p>
    </div>
);

Footer.propTypes = {
    classes: object.isRequired,
    repo: string.isRequired,
    version: string.isRequired
};

export default withStyles(styles)(Footer);
