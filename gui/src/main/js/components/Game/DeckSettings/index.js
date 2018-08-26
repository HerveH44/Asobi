import React from "react";
import {bool} from "prop-types";
import {connect} from "react-redux";
import LandsPanel from "./LandsPanel";
import DownloadPanel from "./DownloadPanel/index";

const DeckSettings = ({didGameStart}) => (
    didGameStart
        ? <div className='deck-settings'>
            <LandsPanel/>
            <DownloadPanel/>
        </div>
        : <div/>
);

DeckSettings.propTypes = {
    didGameStart: bool.isRequired,
};

const mapStateToProps = ({gameState}) => ({
    didGameStart: gameState.didGameStart
});
const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(DeckSettings);
