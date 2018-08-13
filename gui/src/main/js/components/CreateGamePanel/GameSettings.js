import React, { Fragment } from 'react';
import {func, array, string, object, bool} from 'prop-types';
import "./style.css"
import {NORMAL, CUBE, CHAOS} from "../../reducers/game";
import { Textarea, Checkbox } from '../utils';

class GameSettings extends React.Component {

    handleChange = name => event => {
        this.props.editGame({[name]: event.target.value})
    };

    handleChangeCheck = name => event => {
        this.props.editGame({[name]: event.target.checked})
    };

    handleSet = index => event => {
        this.props.editGame({
            sets: {
                ...this.props.sets,
                [this.props.gameType]: this.props.sets[this.props.gameType]
                                            .map((set, i) => i === index ? event.currentTarget.value : set )
            }
        })
    };

    render() {
        const {availableSets, gameMode, gameType, gameModes, sets, cubeList, modernOnly, totalChaos} = this.props;

        return (
            <Fragment >
                <div className="tab">
                    {gameModes.map((mode, key) => (
                        <button key={key} value={mode} className={`tablinks ${gameMode === mode ? "active" : ""}`} onClick={this.handleChange("gameMode")}>
                            {mode}
                        </button>))}
                </div>
                {gameMode === NORMAL && <NormalGameSettings
                    availableSets={availableSets}
                    sets={sets[gameType]}
                    onChangeSet={this.handleSet.bind(this)}
                    />}
                {gameMode === CUBE &&
                    <CubeGameSettings
                        cubeList={cubeList}
                        onChangeCubeList={this.handleChange("cubeList")}/>}
                {gameMode === CHAOS &&
                    <ChaosGameSettings
                        modernOnly={modernOnly}
                        totalChaos={totalChaos}
                        onChangeModernOnly={this.handleChangeCheck("modernOnly")}
                        onChangeTotalChaos={this.handleChangeCheck("totalChaos")}/>}
            </Fragment>
        );
    }
}

GameSettings.propTypes = {
    gameModes: array.isRequired,
    gameMode: string.isRequired,
    gameType: string.isRequired,
    editGame: func.isRequired,
    sets: object.isRequired,
    availableSets: object.isRequired,
    cubeList: string.isRequired,
    modernOnly: bool.isRequired,
    totalChaos: bool.isRequired
};

const NormalGameSettings = ({availableSets, sets, onChangeSet}) => (
    <div className="tabcontent" >
        {sets.map((set, index) => (<Set
            key={index}
            set={set}
            availableSets={availableSets}
            onChangeSet={onChangeSet(index)}/>))}
    </div>
);

NormalGameSettings.propTypes = {
    sets: array.isRequired,
    availableSets: object.isRequired,
    onChangeSet: func.isRequired
};

const CubeGameSettings = ({cubeList, onChangeCubeList}) => (
    <div>
        <div>one card per line</div>
        <Textarea placeholder='cube list' value={cubeList} onChange={onChangeCubeList}/>
    </div>
);

CubeGameSettings.propTypes = {
    cubeList: string.isRequired,
    onChangeCubeList: func.isRequired
};

const ChaosGameSettings = ({modernOnly, totalChaos, onChangeModernOnly, onChangeTotalChaos}) => (
    <div>
        <div>
            <Checkbox
                checked={modernOnly}
                side='right'
                text='Only Modern Sets: '
                onChange={onChangeModernOnly}/>
        </div>
        <div>
            <Checkbox
                checked={totalChaos}
                side='right'
                text='Total Chaos: '
                onChange={onChangeTotalChaos}/>
        </div>
    </div>
);

ChaosGameSettings.propTypes = {
    modernOnly: bool.isRequired,
    totalChaos: bool.isRequired,
    onChangeModernOnly: func.isRequired,
    onChangeTotalChaos: func.isRequired
};


const Set = ({availableSets, set, onChangeSet}) => (
    <select value={set} onChange={onChangeSet}>
        {Object
            .entries(availableSets)
            .map(([label, sets], key) => (
                <optgroup key={key} label={label}>
                    {sets
                        .sort((a, b) => new Date(b.releaseDate).getTime() - new Date(a.releaseDate).getTime())
                        .map(({code, name}, index) => {
                        return <option value={code} key={index}>{name}</option>
                    })}
                </optgroup>
            ))}
    </select>
);

Set.propTypes = {
    set: string.isRequired,
    availableSets: object.isRequired,
    onChangeSet: func.isRequired
};

export default GameSettings;
