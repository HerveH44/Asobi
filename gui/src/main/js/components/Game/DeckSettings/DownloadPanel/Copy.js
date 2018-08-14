import React from "react";
import {string, func} from "prop-types";
import {connect} from "react-redux";
import {getDeckInTxt} from "../../../../reducers/playerState";

const Copy = ({deck,}) => {

    const onClickCopy = () => {
        let textField = document.createElement("textarea");
        textField.value = deck;
        document.body.appendChild(textField);
        textField.select();
        document.execCommand("copy");
        textField.remove();
    };

    return (
        <div className='copy-controls connected-container'>
            <button
                className='connected-component'
                onClick={onClickCopy}>
                Copy deck to clipboard
            </button>
        </div>
    )
};

Copy.propTypes = {
    deck: string.isRequired
};

const mapStateToProps = ({playerState}) => ({
    deck: getDeckInTxt(playerState)
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(Copy);
