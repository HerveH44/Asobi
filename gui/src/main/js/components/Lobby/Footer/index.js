import React from "react";
import {string, object} from "prop-types";

const Footer = ({repo, version}) => (
    <div>
        <p><strong>dr4ft</strong>{" "}
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
        </p>
        <p>
            Running Version{" "}
            <a href={`https://github.com/dr4fters/dr4ft/commit/${version}`}>
                {version}
            </a>
        </p>
    </div>
);

Footer.propTypes = {
    repo: string.isRequired,
    version: string.isRequired
};

export default Footer;
