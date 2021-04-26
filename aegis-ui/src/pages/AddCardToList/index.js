import React, { useState } from 'react';
import { makeStyles, withStyles } from '@material-ui/core/styles';
import { green } from '@material-ui/core/colors';
import { FormControl, NativeSelect, Checkbox, FormControlLabel } from '@material-ui/core';
import InputBase from '@material-ui/core/InputBase';
import { postCardToDB } from '../../services/CardMgmtService';

const useStyles = makeStyles(theme => ({
    holder: {
        width: '1110px',
        height: '935px',
        display: 'flex',
        flexDirection: 'column'
    },
    row: {
        display: 'flex',
        flexDirection: 'row',
        marginLeft: '35px',
        marginTop: '30px'
    },
    selectCardText: {
        textAlign: 'center',
        padding: '8px'
    },
    margin: {
        marginLeft: '50px',
        width: '300px',
        height: '40px'
    },
    marginEmp: {
        marginLeft: '10px',
        width: '300px',
        height: '40px'
    },
    checkbox: {
        marginLeft: '230px',
        marginTop: '25px',
        width: '200px'
    },
    saveButton: {
        width: '75px',
        height: '38px',
        borderRadius: '4px',
        boxShadow: '0 1px 1px 0 rgba(19, 31, 21, 0.1), inset 0 2px 0 0 rgba(255, 255, 255, 0.06)',
        border: 'solid 1px #2d9c3c',
        backgroundImage: 'linear-gradient(to top, #34aa44, #38b249)',
        color: '#fff',
        marginLeft: '235px',
        marginTop: '25px'
    },
}));

const GreenCheckbox = withStyles({
    root: {
        '&$checked': {
            color: green[600],
        },
    },
    checked: {},
})(props => <Checkbox color="default" {...props} />);

const BootstrapInput = withStyles(theme => ({
    input: {
        borderRadius: 4,
        position: 'relative',
        border: '1px solid #ced4da',
        fontSize: 16,
        padding: '10px 26px 10px 12px',
        transition: theme.transitions.create(['border-color', 'box-shadow']),
        '&:focus': {
            borderRadius: 4,
            borderColor: '#80bdff',
            boxShadow: '0 0 0 0.2rem rgba(0,123,255,.25)',
        },
    },
}))(InputBase);

export default function AddCardToList(props) {
    const classes = useStyles();
    const [cardNo, setCardNo] = useState("");
    const [name, setName] = useState("");
    const [checked, setChecked] = React.useState(false);
    const handleChecked = event => {
        setChecked(event.target.checked);
    };

    const handleChangeName = event => {
        setName(event.target.value);
    }

    const handleAge = event => {
        setCardNo(event.target.value);
    }

    const addCardToSystem = async () => {
        await postCardToDB(cardNo, name);
        props.setRender(!props.render);
        props.addCard(false);
        props.setOpen(true);
    }
    return (
        <div className={classes.holder}>
            <div className={classes.row}>
                <div className={classes.selectCardText}>SELECT THE CARD:</div>
                <FormControl className={classes.margin}>
                    <NativeSelect
                        id="demo-customized-select-native"
                        value={cardNo}
                        onChange={handleAge}
                        input={<BootstrapInput />}
                    >
                        <option value="" >Select Any</option>
                        <option value={'CTPO130'}>CTPO130</option>
                        <option value={'CTPO131'}>CTPO131</option>
                        <option value={'CTPO132'}>CTPO132</option>
                        <option value={'CTPO136'}>CTPO136</option>
                        <option value={'CTPO133'}>CTPO133</option>
                        <option value={'CTPO134'}>CTPO134</option>
                        <option value={'CTPO135'}>CTPO135</option>
                    </NativeSelect>
                </FormControl>
            </div>
            <FormControlLabel
                className={classes.checkbox}
                control={
                    <GreenCheckbox
                        checked={checked}
                        onChange={handleChecked}
                        value="checkedG"
                    />
                }
                label="Assign to Employee"
            />
            {checked ?
                <div className={classes.row}>
                    <div className={classes.selectCardText}>SELECT THE EMPLOYEE:</div>
                    <FormControl className={classes.marginEmp}>
                        <NativeSelect
                            id="demo-customized-select-native"
                            value={name}
                            onChange={handleChangeName}
                            input={<BootstrapInput />}
                        >
                            <option value="" >Select Any</option>
                            <option value={'John'}>John</option>
                            <option value={'Dave'}>Dave</option>
                            <option value={'Bravo'}>Bravo</option>
                        </NativeSelect>
                    </FormControl>
                </div>
                : null}
            <button className={classes.saveButton} onClick={() => addCardToSystem()}>Save</button>
        </div>
    )
}